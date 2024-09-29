package org.example.demoproductservice.domain.repository;

import org.example.demoproductservice.domain.repository.entity.Brand;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class BrandRepositoryStub implements BrandRepository {

    private final Map<Long, Brand> brands = new HashMap<>();
    private final AtomicLong brandIdGenerator = new AtomicLong(1);

    public void prepareTestData() {
        // 브랜드 A~I 등록
        int asciiCodeOfA = 'A';
        for (int i = 0; i < 9; i++) {
            String brandName = String.valueOf((char) (asciiCodeOfA + i));
            Brand brand = new Brand(brandIdGenerator.getAndIncrement(), brandName, null);
            brands.put(brand.getId(), brand);
        }
    }

    @Override
    public List<Brand> findAll() {
        return brands.values().stream().toList();
    }


    @Override
    public void deleteAll() {
        brands.clear();
    }

    @Override
    public <S extends Brand> S save(S entity) {
        // id
        Long id = entity.getId();
        if (id == null) {
            id = brandIdGenerator.getAndIncrement();
            Brand brand = new Brand(id, entity.getBrandName());
            brands.put(id, brand);
        } else if (brands.get(entity.getId()) != null) {
            brands.put(id, entity);
        } else {
            return null;
        }
        return (S) brands.get(id);
    }

    @Override
    public Optional<Brand> findById(Long aLong) {
        Brand brand = brands.get(aLong);
        if (brand == null) {
            return Optional.empty();
        }
        return Optional.of(brand);
    }

    /**
     * 이하 메서드는 미구현
     */
    @Override
    public void flush() {

    }

    @Override
    public <S extends Brand> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Brand> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Brand> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Brand getOne(Long aLong) {
        return null;
    }

    @Override
    public Brand getById(Long aLong) {
        return null;
    }

    @Override
    public Brand getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Brand> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Brand> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Brand> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Brand> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Brand> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Brand> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Brand, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    @Override
    public <S extends Brand> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }


    @Override
    public boolean existsById(Long aLong) {
        return false;
    }


    @Override
    public List<Brand> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Brand entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Brand> entities) {

    }


    @Override
    public List<Brand> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return null;
    }
}
