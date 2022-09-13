package br.com.erudio.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapper {

    private static final Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();

    private DozerMapper() { }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return MAPPER.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origins, Class<D> destination) {
        List<D> destinations = new ArrayList<>();

        for (O o : origins) {
            destinations.add(MAPPER.map(o, destination));
        }

        return destinations;
    }
}
