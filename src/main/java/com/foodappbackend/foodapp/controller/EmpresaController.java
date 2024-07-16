package com.foodappbackend.foodapp.controller;

import com.foodappbackend.foodapp.entity.Empresa;
import com.foodappbackend.foodapp.service.IEmpresaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@AllArgsConstructor
public class EmpresaController {

    @Autowired
    private IEmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresaList = empresaService.findAll();
        return new ResponseEntity<>(empresaList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Empresa> findEmpresaById(@PathVariable("id") Long id) {
        Empresa empresa = empresaService.findById(id);
        return new ResponseEntity<>(empresa, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable("id") Long id, @RequestBody Empresa empresa) {
        empresa.setId(id);
        Empresa updatedEmpresa = empresaService.updateEmpresa(empresa);
        return new ResponseEntity<>(updatedEmpresa, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmpresa(@PathVariable("id") Long id) {
        empresaService.deleteById(id);
        return new ResponseEntity<>("Empresa was deleted successfully", HttpStatus.OK);
    }
}
