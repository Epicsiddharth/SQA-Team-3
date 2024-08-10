package mypack.controllers;

import mypack.models.*;
import mypack.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {//http://localhost:8080/swagger-ui.html

	//important
	@Autowired
	private StudentService studentService;

	// Create a new student
	@Operation(summary = "Create a new student in memory database.")
	@ApiResponse(description = "Return status created" , responseCode = "201")
	@ApiResponse(description = "Return internal server error." , responseCode = "500")
	@PostMapping
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		if (studentService.save(student)) {
			return new ResponseEntity<>(student, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// Get all students
	@Operation(summary = "Getting all the students from the database")
	@ApiResponse(description = "Return a list of students" , responseCode = "200")
	//Swagger Documentation
	@GetMapping
	public List<Student> getAllStudents() {
		return studentService.findAll();
	}

	// Get a student by ID
	@Operation(summary = "Getting student by Id")
	@ApiResponse(description = "Return a list of students" , responseCode = "200")
	@ApiResponse(description = "Return 404 if not student with that Id" , responseCode = "404")
	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
		Optional<Student> student = studentService.findByID(id);
		return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Delete a student
	@Operation(summary = "Delete student by Id")
	@ApiResponse(description = "Delete student by Id. Return Ok" , responseCode = "200")
	@ApiResponse(description = "Return 404 if not student with that Id" , responseCode = "404")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		if (studentService.existsById(id) && studentService.delete(id)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}
}
