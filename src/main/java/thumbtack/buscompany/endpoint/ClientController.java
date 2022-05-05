package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.ClientRegisterResponse;
import thumbtack.buscompany.service.ClientService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService service;
    @PostMapping
    public ClientRegisterResponse register(@Valid @RequestBody ClientRegisterRequest request){
        return service.register(request);
    }
}
