package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.dto.task.UpsertTaskRequest;
import com.example.tasktracker.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {
    Task requestToTask(UpsertTaskRequest request);

    UpsertTaskRequest taskToRequest(Task task);

    TaskResponse taskToResponse(Task task);

    List<TaskResponse> taskToResponse(List<Task> tasks);

    void update(UpsertTaskRequest source, @MappingTarget Task target);


}
