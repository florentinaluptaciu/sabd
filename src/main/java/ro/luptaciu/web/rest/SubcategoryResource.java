package ro.luptaciu.web.rest;

import com.codahale.metrics.annotation.Timed;
import ro.luptaciu.domain.Subcategory;

import ro.luptaciu.repository.SubcategoryRepository;
import ro.luptaciu.web.rest.util.HeaderUtil;
import ro.luptaciu.service.dto.SubcategoryDTO;
import ro.luptaciu.service.mapper.SubcategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Subcategory.
 */
@RestController
@RequestMapping("/api")
public class SubcategoryResource {

    private final Logger log = LoggerFactory.getLogger(SubcategoryResource.class);
        
    @Inject
    private SubcategoryRepository subcategoryRepository;

    @Inject
    private SubcategoryMapper subcategoryMapper;

    /**
     * POST  /subcategories : Create a new subcategory.
     *
     * @param subcategoryDTO the subcategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subcategoryDTO, or with status 400 (Bad Request) if the subcategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subcategories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubcategoryDTO> createSubcategory(@RequestBody SubcategoryDTO subcategoryDTO) throws URISyntaxException {
        log.debug("REST request to save Subcategory : {}", subcategoryDTO);
        if (subcategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subcategory", "idexists", "A new subcategory cannot already have an ID")).body(null);
        }
        Subcategory subcategory = subcategoryMapper.subcategoryDTOToSubcategory(subcategoryDTO);
        subcategory = subcategoryRepository.save(subcategory);
        SubcategoryDTO result = subcategoryMapper.subcategoryToSubcategoryDTO(subcategory);
        return ResponseEntity.created(new URI("/api/subcategories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subcategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subcategories : Updates an existing subcategory.
     *
     * @param subcategoryDTO the subcategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subcategoryDTO,
     * or with status 400 (Bad Request) if the subcategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the subcategoryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subcategories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubcategoryDTO> updateSubcategory(@RequestBody SubcategoryDTO subcategoryDTO) throws URISyntaxException {
        log.debug("REST request to update Subcategory : {}", subcategoryDTO);
        if (subcategoryDTO.getId() == null) {
            return createSubcategory(subcategoryDTO);
        }
        Subcategory subcategory = subcategoryMapper.subcategoryDTOToSubcategory(subcategoryDTO);
        subcategory = subcategoryRepository.save(subcategory);
        SubcategoryDTO result = subcategoryMapper.subcategoryToSubcategoryDTO(subcategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subcategory", subcategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subcategories : get all the subcategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subcategories in body
     */
    @RequestMapping(value = "/subcategories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SubcategoryDTO> getAllSubcategories() {
        log.debug("REST request to get all Subcategories");
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        return subcategoryMapper.subcategoriesToSubcategoryDTOs(subcategories);
    }

    /**
     * GET  /subcategories/:id : get the "id" subcategory.
     *
     * @param id the id of the subcategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subcategoryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subcategories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubcategoryDTO> getSubcategory(@PathVariable Long id) {
        log.debug("REST request to get Subcategory : {}", id);
        Subcategory subcategory = subcategoryRepository.findOne(id);
        SubcategoryDTO subcategoryDTO = subcategoryMapper.subcategoryToSubcategoryDTO(subcategory);
        return Optional.ofNullable(subcategoryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subcategories/:id : delete the "id" subcategory.
     *
     * @param id the id of the subcategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subcategories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        log.debug("REST request to delete Subcategory : {}", id);
        subcategoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subcategory", id.toString())).build();
    }

}
