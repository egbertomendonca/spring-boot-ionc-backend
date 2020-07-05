package com.estudo.cursomc.services.validation;

import com.estudo.cursomc.domain.Cliente;
import com.estudo.cursomc.domain.enums.TipoCliente;
import com.estudo.cursomc.dto.ClienteDTO;
import com.estudo.cursomc.repositories.ClienteRepository;
import com.estudo.cursomc.resources.exception.FieldMessage;
import com.estudo.cursomc.services.validation.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteUpdate ann) {
    }

    @Override
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        // inclua os testes aqui, inserindo erros na lista

        Map<String,String> map = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
        if(aux != null && !aux.getId().equals(uriId)){
            list.add(new FieldMessage("email", "Email já existente"));
        }
        //
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}