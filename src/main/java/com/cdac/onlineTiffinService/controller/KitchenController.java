package com.cdac.onlineTiffinService.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.onlineTiffinService.dto.KitchenResponseDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationRequestDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationResponseDto;
import com.cdac.onlineTiffinService.model.Kitchen;
import com.cdac.onlineTiffinService.service.KitchenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kitchens")
@RequiredArgsConstructor

public class KitchenController {
	private final KitchenService kitchenService;
	
	@PostMapping
	public ResponseEntity<UserKitchenRegistrationResponseDto> createKitchen(@Valid @RequestBody UserKitchenRegistrationRequestDto req){
		UserKitchenRegistrationResponseDto response = kitchenService.createKitchen(req);	
		return new ResponseEntity<UserKitchenRegistrationResponseDto>(response , HttpStatus.CREATED); //201
		
				
	}
	/*
	@GetMapping
	public ResponseEntity<?> getAllKitchens(){
		List<KitchenResponseDto> kitchens = kitchenService.getAllKitchens();
		return ResponseEntity.ok(kitchens); // 200
	}
	*/
	
	@GetMapping
	public ResponseEntity<?> getAllKitchens(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			
			){
		Page<KitchenResponseDto> kitchens = kitchenService.getAllKitchens(page,size);
		return ResponseEntity.ok(kitchens); // 200
	}
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getKitchenById(@PathVariable Long id){
		return ResponseEntity.ok(kitchenService.getKitchenById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateKitchen(@PathVariable Long id , @Valid @RequestBody UserKitchenRegistrationRequestDto req){
		UserKitchenRegistrationResponseDto response = kitchenService.updateKitchen(id, req);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteKitchen(@PathVariable Long id){
		kitchenService.deleteKitchen(id);
		
		return ResponseEntity.ok("Kitchen deleted successfully !!");
	}
	
	/*
	 
	  @GetMapping("/city/{city}")
	public ResponseEntity<?> getKitchenByCity(@PathVariable String city){
		return ResponseEntity.ok(kitchenService.getKitchenByCity(city));
		
	}
	  
	 */
	
	@GetMapping("/city/{city}")
	public ResponseEntity<?> getKitchenByCity(
			@PathVariable String city,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			){
		return ResponseEntity.ok(kitchenService.getKitchenByCity(city,page,size));
		
	}
	
	
	
	/*
	@GetMapping("/active")
	public ResponseEntity<?> getAllActiveKitchens(){
		return ResponseEntity.ok(kitchenService.getKitchenByActive());
	}
	*/
	@GetMapping("/active")
	public ResponseEntity<?> getAllActiveKitchens(
			
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			){
		return ResponseEntity.ok(kitchenService.getKitchenByActive(page , size));
	}
	
	@GetMapping("/notactive")
	public ResponseEntity<?> getAllNonActiveKitchens(){
		return ResponseEntity.ok(kitchenService.getKitchenByNotActive());
	}
	
	@PutMapping("/{id}/block")
	public ResponseEntity<KitchenResponseDto> blockKitchen(
	        @PathVariable Long id) {

	    KitchenResponseDto response = kitchenService.blockKitchen(id);

	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}/unblock")
	public ResponseEntity<KitchenResponseDto> unblockKitchen(
	        @PathVariable Long id) {

	    KitchenResponseDto response = kitchenService.unblockKitchen(id);

	    return ResponseEntity.ok(response);
	}
}
