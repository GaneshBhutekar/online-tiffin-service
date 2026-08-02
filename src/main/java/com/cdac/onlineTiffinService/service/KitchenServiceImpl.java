package com.cdac.onlineTiffinService.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.cdac.onlineTiffinService.dto.KitchenResponseDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationRequestDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationResponseDto;
import com.cdac.onlineTiffinService.dto.UserResponseDto;
import com.cdac.onlineTiffinService.exceptions.ForbiddenException;
import com.cdac.onlineTiffinService.exceptions.ResourceNotFoundException;
import com.cdac.onlineTiffinService.model.Kitchen;
import com.cdac.onlineTiffinService.model.Role;
import com.cdac.onlineTiffinService.model.User;
import com.cdac.onlineTiffinService.repository.KitchenRepository;
import com.cdac.onlineTiffinService.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class KitchenServiceImpl implements KitchenService{

	private final KitchenRepository kitchenRepository;
	
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	private final EmailService emailService;

	// Mirrors MenuServiceImpl: the JWT filter stores the logged-in user's id as the
	// Authentication principal. hasAnyRole("KITCHEN","ADMIN") alone only proves the
	// caller owns *some* kitchen, not that they own *this* one - so we check here too.
	private void verifyKitchenOwnership(Kitchen kitchen) {
		Long currentUserId = (Long) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		boolean isAdmin = SecurityContextHolder.getContext()
				.getAuthentication()
				.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin
				&& (kitchen.getOwner() == null
					|| !kitchen.getOwner().getId().equals(currentUserId))) {
			throw new ForbiddenException(
					"You are not authorized to modify this kitchen.");
		}
	}


	@Override
	public UserKitchenRegistrationResponseDto createKitchen(UserKitchenRegistrationRequestDto req) {
		User user = modelMapper.map(req.getUser(),User.class);
		Kitchen kitchen = modelMapper.map(req.getKitchen(), Kitchen.class);
		
		// encode raw password before persisting so BCrypt-based login works
		user.setPassword(passwordEncoder.encode(req.getUser().getPassword()));
		// force KITCHEN role for owners registered through this endpoint
		user.setRole(Role.KITCHEN);
		
		// copy all from the dto gtter and use setter of kitchen to create Ktchen object , now we have to save it to the database
		User savedUser = userRepository.save(user);
		kitchen.setOwner(savedUser);
		Kitchen savedKitchen = kitchenRepository.save(kitchen);
		UserKitchenRegistrationResponseDto  response= new UserKitchenRegistrationResponseDto();
		response.setUser(modelMapper.map(savedUser, UserResponseDto.class));
		response.setKitchen(modelMapper.map(savedKitchen, KitchenResponseDto.class));
		
		
		// now in response we will do 
		try {
			emailService.sendEmail(
					savedUser.getEmail(),
					"Your Kitchen Has Been Registered!",
					"Hi " + savedUser.getName() + ",\n\n"
							+ "Your kitchen \"" + savedKitchen.getKitchenName() + "\" has been registered successfully "
							+ "on Online Tiffin Service.\n"
							+ "You can now add menu items and start receiving orders.\n\n"
							+ "Thanks,\nOnline Tiffin Service Team"
			);
		} catch (Exception e) {
			// do not fail kitchen registration if email sending fails
			System.err.println("Failed to send kitchen registration email: " + e.getMessage());
		}
		
		return response;
		
	}
	
	/*
	@Override
	public List<KitchenResponseDto> getAllKitchens(){
		List<Kitchen> Kitchens = kitchenRepository.findAll();
		// instead of copy pasting the kitchen data to kitchenResponse by loop we will use map a stream functioin
		
		
		return Kitchens.stream().map(kit -> modelMapper.map(kit, KitchenResponseDto.class)).toList();
	
	}
	*/
	
	// after pagination
	
	@Override
	public Page<KitchenResponseDto> getAllKitchens(int page,int size){
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Kitchen> kitchens = kitchenRepository.findAll(pageable);
		// instead of copy pasting the kitchen data to kitchenResponse by loop we will use map a stream functioin
		
		
//		return Kitchens.stream().map(kit -> modelMapper.map(kit, KitchenResponseDto.class)).toList();
		
		// dont need stream cause page already have map function in it , so dont need to stream
		return kitchens.map(
	            kitchen ->
	                    modelMapper.map(kitchen,
	                            KitchenResponseDto.class));
	}
	
	@Override
	public KitchenResponseDto getKitchenById(Long id) {
		Kitchen kitchen = kitchenRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Kitchen", "id", id));
		return modelMapper.map(kitchen, KitchenResponseDto.class);
	}


	@Override
	public UserKitchenRegistrationResponseDto updateKitchen(Long id, UserKitchenRegistrationRequestDto req) {
		Kitchen kitchen = kitchenRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Kitchen", "id", id));
		verifyKitchenOwnership(kitchen);
		Long owner_id = kitchen.getOwner().getId();
		
		User user = userRepository.findById(owner_id).orElseThrow(()->new ResourceNotFoundException("User", "id", owner_id));
		
		
		user.setName(req.getUser().getName());
		user.setCity(req.getUser().getCity());
		user.setEmail(req.getUser().getEmail());
		user.setPassword(passwordEncoder.encode(req.getUser().getPassword()));
		user.setPhoneNumber(req.getUser().getPhoneNumber());
		
		
		kitchen.setKitchenName(req.getKitchen().getKitchenName());
		kitchen.setKitchenCity(req.getKitchen().getKitchenCity());
		kitchen.setAddress(req.getKitchen().getAddress());
		kitchen.setDescription(req.getKitchen().getDescription());
		
		UserResponseDto userDto = modelMapper.map(user,UserResponseDto.class);
		KitchenResponseDto kitchenDto= modelMapper.map(kitchen, KitchenResponseDto.class);
		UserKitchenRegistrationResponseDto response = new UserKitchenRegistrationResponseDto();
		response.setKitchen(kitchenDto);
		response.setUser(userDto);
		
		
		return response;
			
	}
	
	@Override
	public void deleteKitchen(Long id) {
		Kitchen kitchen = kitchenRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Kitchen", "id", id));
		verifyKitchenOwnership(kitchen);
		// delete this kitchen
		
		
		// deletet by id is also good but have to check fuirst id is there in the DB or not
		kitchenRepository.delete(kitchen);
	}
	
	
	/*
	 
	 
	 @Override
	public List<KitchenResponseDto> getKitchenByCity(String city){
		List<Kitchen> kitchens = kitchenRepository.findBykitchenCity(city);
		
		return kitchens.stream().map(kit -> modelMapper.map(kit, KitchenResponseDto.class)).toList();
	}
	 
	 */
	@Override
	public Page<KitchenResponseDto> getKitchenByCity(String city,int page , int size){
		
		Pageable pageable = PageRequest.of(page, size);
		
		
		Page<Kitchen> kitchens = kitchenRepository.findBykitchenCity(city,pageable);
		
//		return kitchens.stream().map(kit -> modelMapper.map(kit, KitchenResponseDto.class)).toList();
		return kitchens.map(kit ->
        modelMapper.map(
                kit,
                KitchenResponseDto.class));
	}

	
	/*
	@Override
	public List<KitchenResponseDto> getKitchenByActive() {
		// TODO Auto-generated method stub
		List<Kitchen> kitchens = kitchenRepository.findByBlockedFalse();
		
		return kitchens.stream().map(kit-> modelMapper.map(kit, KitchenResponseDto.class)).toList();
		
	}
	
	*/
	
	@Override
	public Page<KitchenResponseDto> getKitchenByActive(int page , int size) {
		// TODO Auto-generated method stub
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Kitchen> kitchens = kitchenRepository.findByBlockedFalse(pageable);
		
//		return kitchens.stream().map(kit-> modelMapper.map(kit, KitchenResponseDto.class)).toList();
		return kitchens.map(
	            kit -> modelMapper.map(
	                    kit,
	                    KitchenResponseDto.class));
		
	}
	@Override
	public List<KitchenResponseDto> getKitchenByNotActive() {
		// TODO Auto-generated method stub
		List<Kitchen> kitchens = kitchenRepository.findByBlockedTrue();
		
		return kitchens.stream().map(kit-> modelMapper.map(kit, KitchenResponseDto.class)).toList();
		
	}
	@Override
	public KitchenResponseDto blockKitchen(Long id) {

	    Kitchen kitchen = kitchenRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Kitchen",
	                            "id",
	                            id));

	    kitchen.setBlocked(true);

	    return modelMapper.map(kitchen, KitchenResponseDto.class);
	}
	
	@Override
	public KitchenResponseDto unblockKitchen(Long id) {

	    Kitchen kitchen = kitchenRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Kitchen",
	                            "id",
	                            id));

	    kitchen.setBlocked(false);

	    return modelMapper.map(kitchen, KitchenResponseDto.class);
	}
	
	
	
	
	
}
