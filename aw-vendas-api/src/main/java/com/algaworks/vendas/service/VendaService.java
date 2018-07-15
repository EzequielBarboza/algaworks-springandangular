package com.algaworks.vendas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.vendas.model.Venda;
import com.algaworks.vendas.repository.Produtos;
import com.algaworks.vendas.repository.Vendas;

@Service
public class VendaService {

	@Autowired
	private Vendas vendas;

	@Autowired
	private Produtos produtos;

	public Venda adicionar(final Venda venda) {
		venda.setCadastro(LocalDateTime.now());
		venda.getItems().forEach(i -> {
			i.setVenda(venda);
			i.setProduto(produtos.findById(i.getProduto().getId()).get());
		});

		BigDecimal totalItems = venda.getItems().stream()
				.map(i -> i.getProduto().getValor().multiply(new BigDecimal(i.getQuantidade())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		venda.setTotal(totalItems.add(venda.getFrete()));
		return vendas.save(venda);
	}
}
