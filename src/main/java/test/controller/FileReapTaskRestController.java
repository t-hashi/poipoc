package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import test.service.FileReapTaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@RestController
@AllArgsConstructor
@RequestMapping("/filereap")
public class FileReapTaskRestController {

	@Autowired
    protected FileReapTaskService fileReapTaskService;

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewTask() {
    	fileReapTaskService.fileReap();
        return "fileReapTaskService.fileReap() task start success";
    }

    @ExceptionHandler(TaskRejectedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handle() {
        return "fileReapTaskService.fileReap() task start error";
    }

}