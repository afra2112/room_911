package com.room_911;

import com.room_911.entity.Admin;
import com.room_911.entity.ProductionDepartment;
import com.room_911.repository.AdminRepository;
import com.room_911.repository.ProductionDepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Room911Application {

	public static void main(String[] args) {
		SpringApplication.run(Room911Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder, AdminRepository adminRepository, ProductionDepartmentRepository productionDepartmentRepository) {
		return args -> {
			if (adminRepository.findByUsernameAndActiveAndDeletedFalse("andres_admin_room_911", true) == null) {
				Admin admin = new Admin();
				
				admin.setActive(true);
				admin.setUsername("andres_admin_room_911");
				admin.setPassword(passwordEncoder.encode("andres"));
				admin.setAdminName("Andres Ramirez");
				adminRepository.save(admin);
			}

			if(productionDepartmentRepository.findAll().isEmpty()) {
				for (int i = 0; i < 5; i ++){
					ProductionDepartment productionDepartment = new ProductionDepartment();
					productionDepartment.setName("Departamento: "+ (i+1));
					productionDepartmentRepository.save(productionDepartment);
				}
			}
		};
	}
}
