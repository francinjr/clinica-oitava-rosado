package com.francinjr.clinicaoitavarosado.mappers;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.francinjr.clinicaoitavarosado.dtos.paciente.PacienteDto;
import com.francinjr.clinicaoitavarosado.entities.Paciente;

public class Mapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	static {
        mapper.createTypeMap(
                Paciente.class,
                PacienteDto.class)
            .addMapping(Paciente::getId, PacienteDto::setKey);
        mapper.createTypeMap(
        		PacienteDto.class,
            Paciente.class)
            .addMapping(PacienteDto::getKey, Paciente::setId);
	}
	
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		return destinationObjects;
	}

}
