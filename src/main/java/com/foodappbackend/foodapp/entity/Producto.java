package com.foodappbackend.foodapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.foodappbackend.foodapp.enums.TipoProducto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Data
@Table(name = "productos")
@Audited
public class Producto  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String codProducto;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Double precioOriginal;

    @Column(nullable = false)
    private Double precioOferta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProducto tipoProducto;

    @Column(nullable = false)
    private Integer cant_disponible;

   @ManyToOne(fetch = FetchType.LAZY)
   @JsonBackReference
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private Empresa empresa;
}
