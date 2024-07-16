package com.foodappbackend.foodapp.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.*;

@Entity
@Data
@Table(name = "empresas")
@Audited
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @Column(nullable = false)
    @NotBlank(message = "El horario es obligatorio")
    private String horario;


    private String username;

    private String password;

    @Column(nullable = false)
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String logo;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Producto> productos = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @NotAudited
    @JoinTable(
            name = "empresa_roles",
            joinColumns = @JoinColumn(name = "empresa_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Rol> roles = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(id, empresa.id) && Objects.equals(username, empresa.username) && Objects.equals(password, empresa.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }


}
