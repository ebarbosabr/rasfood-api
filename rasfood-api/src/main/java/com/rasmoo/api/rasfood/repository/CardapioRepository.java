package com.rasmoo.api.rasfood.repository;

import com.rasmoo.api.rasfood.dto.CardapioDTO;
import com.rasmoo.api.rasfood.entity.Cardapio;
import com.rasmoo.api.rasfood.repository.projection.CardapioProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardapioRepository extends PagingAndSortingRepository<Cardapio, Integer>, JpaRepository<Cardapio, Integer> { //JpaRepository

    @Query(value = "SELECT new com.rasmoo.api.rasfood.dto.CardapioDTO(c.nome, c.descricao, c.valor, c.categoria.nome) FROM Cardapio c WHERE c.disponivel = true AND c.nome LIKE %:nome%")
    List<CardapioDTO> findAllByNome(String nome);

    @Query(value = "SELECT * " +
                   "FROM Cardapio c " +
                   "WHERE c.categoria_id = ?1 " +
                   "AND c.disponivel = 1", nativeQuery=true)
    List<Cardapio> findAllByCategoria(Integer categoriaId);

    @Query(value = "SELECT" +
            "    c.nome as nome," +
            "    c.descricao as descricao," +
            "    c.valor as valor," +
            "    cat.nome as nomeCategoria" +
            "    FROM cardapio c" +
            "    INNER JOIN categorias cat on c.categoria_id = cat.id" +
            "    WHERE c.categoria_id = ?1 AND c.disponivel = true",nativeQuery = true)
    List<CardapioProjection> findAllByCategoriaIdProj(final Integer categoria);
}
