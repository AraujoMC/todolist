package ara.com.aula.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ara.com.aula.model.TaskModel;
import ara.com.aula.repository.ITaskRepository;
import ara.com.aula.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/tasks")
public class taskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
      
        var task = taskRepository.findByTitle(taskModel.getTitle());
        var userID = request.getAttribute("userID");
        taskModel.setUserID((UUID) userID);
       
        var currentDate = LocalDateTime.now();
        if (task != null) {
                return ResponseEntity.status(400).body("já existe essa tarefa");
        }
        else if (currentDate.isAfter(taskModel.getStartAt())) {
            return ResponseEntity.status(400).body("Data de começo não pode ser antes da data atual");
        }
        else if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(400).body("A data de término não pode ser antes da data de início da tarefa");
        }

        this.taskRepository.save(taskModel);
        return ResponseEntity.status(200).body("Tarefa criada");
    }

    @GetMapping("/")
    public List<TaskModel> listALL(HttpServletRequest request){
        var userID = request.getAttribute("userID");
        var tasks =  taskRepository.findByUserID((UUID) userID);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update (@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var userID = request.getAttribute("userID");
       
        var task = this.taskRepository.findById(id).orElse(null);

        if(task == null){
            return ResponseEntity.status(400).body("Tarefa não existe");
        }
        if (!task.getUserID().equals(userID)) {
            return ResponseEntity.status(400).body("Usuario nao tem autorizacao para alterar tarefa");
        }
        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
    
}
