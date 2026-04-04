package bg.uni.fmi.theatre.controller;

import bg.uni.fmi.theatre.dto.ShowRequest;
import bg.uni.fmi.theatre.dto.ShowResponse;
import bg.uni.fmi.theatre.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@Tag(name = "Shows Api")
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get show by id", description = "Returns a show by its id")
    @ApiResponse(responseCode = "200", description = "Show found")
    @ApiResponse(responseCode = "404", description = "Show not found")
    public ShowResponse getShowById(@PathVariable Long id) {
        return showService.getShowById(id);
    }

    @GetMapping
    @Operation(summary = "Get all shows", description = "Returns a list of all shows")
    @ApiResponse(responseCode = "200", description = "Shows retrieved successfully")
    public List<ShowResponse> listAllShows() {
        return showService.getAllShows();
    }

    @PostMapping
    @Operation(summary = "Create a new show", description = "Creates a new show in the system")
    @ApiResponse(responseCode = "201", description = "Show created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<ShowResponse> createShow(@Valid @RequestBody ShowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.addShow(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing show", description = "Updates an existing show by its id")
    @ApiResponse(responseCode = "200", description = "Show updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Show not found")
    public ShowResponse updateShow(@PathVariable(name = "id") Long id, @Valid @RequestBody ShowRequest request) {
        return showService.updateShow(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a show", description = "Deletes an existing show by its id")
    @ApiResponse(responseCode = "204", description = "Show deleted successfully")
    @ApiResponse(responseCode = "404", description = "Show not found")
    public ResponseEntity<Void> deleteShow(@PathVariable(name = "id") Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}
