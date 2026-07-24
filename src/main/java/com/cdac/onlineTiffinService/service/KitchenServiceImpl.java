package com.cdac.onlineTiffinService.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cdac.onlineTiffinService.dto.KitchenResponseDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationRequestDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationResponseDto;
import com.cdac.onlineTiffinService.dto.UserResponseDto;
import com.cdac.onlineTiffinService.exceptions.ResourceNotFoundException;
import com.cdac.onlineTiffinService.model.Kitchen;
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
	
	
	@Override
	public UserKitchenRegistrationResponseDto createKitchen(UserKitchenRegistrationRequestDto req) {
		User user = modelMapper.map(req.getUser(),User.class);
		Kitchen kitchen = modelMapper.map(req.getKitchen(), Kitchen.class);
		
		
		// copy all from the dto gtter and use setter of kitchen to create Ktchen object , now we have to save it to the database
		User savedUser = userRepository.save(user);
		kitchen.setOwner(savedUser);
		Kitchen savedKitchen = kitchenRepository.save(kitchen);
		UserKitchenRegistrationResponseDto  response= new UserKitchenRegistrationResponseDto();
		response.setUser(modelMapper.map(savedUser, UserResponseDto.class));
		response.setKitchen(modelMapper.map(savedKitchen, KitchenResponseDto.class));
		
		
		// now in response we will do 
		
		
		return response;
		
	}
	
	
	@Override
	public List<KitchenResponseDto> getAllKitchens(){
		List<Kitchen> Kitchens = kitchenRepository.findAll();
		// instead of copy pasting the kitchen data to kitchenResponse by loop we will use map a stream functioin
		
		
		return Kitchens.stream().map(kit -> modelMapper.map(kit, KitchenResponseDto.class)).toList();
	
	}
	
	@Override
	public KitchenResponseDto getKitchenById(Long id) {
		Kitchen kitchen = kitchenRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Kitchen", "id", id));
		return modelMapper.map(kitchen, KitchenResponseDto.class);
	}


	@Override
	public UserKitchenRegistrationResponseDto updateKitchen(Long id, UserKitchenRegistrationRequestDto req) {
		Kitchen kitchen = kitchenRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Kitchen", "id", id));
		Long owner_id = kitchen.getOwner().getId();
		
		User user = userRepository.findById(owner_id).orElseThrow(()->new ResourceNotFoundException("User", "id", owner_id));
		
		
		user.setName(req.getUser().getName());
		user.setCity(req.getUser().getCity());
		user.setEmail(req.getUser().getEmail());
		user.setPassword(req.getUser().getPassword());
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
		// delete this kitchen
		
		
		// deletet by id is also good but have to check fuirst id is there in the DB or not
		kitchenRepository.delete(kitchen);
	}
	
	@Override
	public List<KitchenResponseDto> getKitchenByCity(String city){
		List<Kitchen> kitchens = kitchenRepository.findBykitchenCity(city);
		
		return kitchens.stream().map(kit -> modelMapper.map(kit, KitchenResponseDto.class)).toList();
	}


	@Override
	public List<KitchenResponseDto> getKitchenByActive() {
		// TODO Auto-generated method stub
		List<Kitchen> kitchens = kitchenRepository.findByBlockedFalse();
		
		return kitchens.stream().map(kit-> modelMapper.map(kit, KitchenResponseDto.class)).toList();
		
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
