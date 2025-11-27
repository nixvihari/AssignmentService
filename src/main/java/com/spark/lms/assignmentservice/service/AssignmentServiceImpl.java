package com.spark.lms.assignmentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.spark.lms.assignmentservice.dto.AssignmentDTO;
import com.spark.lms.assignmentservice.entity.Assignment;
import com.spark.lms.assignmentservice.repository.AssignmentRepository;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository repo;
    private final ObjectMapper mapper;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository repo, ObjectMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public AssignmentDTO createAssignment(AssignmentDTO dto, Long teacherId) {
        Assignment entity = dtoToEntity(dto);
        entity.setCreatedBy(teacherId);
        Assignment saved = repo.save(entity);
        return entityToDto(saved);
    }

    @Override
    public AssignmentDTO getAssignment(Long id) {
        Assignment a = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
        return entityToDto(a);
    }

    @Override
    public List<AssignmentDTO> listAssignmentsByCourse(Long courseId) {
        List<Assignment> list = repo.findByCourseId(courseId);
        return list.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    // NEW: list all assignments
    @Override
    public List<AssignmentDTO> listAllAssignments() {
        List<Assignment> list = repo.findAll();
        return list.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    // NEW: delete an assignment
    @Override
    public void deleteAssignment(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Assignment not found");
        }
        repo.deleteById(id);
    }

    // mapping helpers using Jackson's ObjectMapper
    private Assignment dtoToEntity(AssignmentDTO dto) {
        return mapper.convertValue(dto, Assignment.class);
    }

    private AssignmentDTO entityToDto(Assignment entity) {
        return mapper.convertValue(entity, AssignmentDTO.class);
    }
}
