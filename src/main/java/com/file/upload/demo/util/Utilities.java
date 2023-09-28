package com.file.upload.demo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilities {

    public Stream<Path> toStream(DirectoryStream<Path> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @SneakyThrows
    public Optional<BasicFileAttributes> toBasicFileAttributes(Path path) {
        return Optional.ofNullable(Files.readAttributes(path, BasicFileAttributes.class));
    }

    public Long toSize(Optional<BasicFileAttributes> basicFileAttributes) {
        return basicFileAttributes.map(BasicFileAttributes::size)
                                  .orElse(0L);
    }

    public final Function<Long, String> toSizeUnit = FileUtils::byteCountToDisplaySize;

    @SneakyThrows
    public static <T, S> List<Predicate<T>> generatePredicates(Class<T> objectClass, S searchCriteria) {
        List<Predicate<T>> predicates = new ArrayList<>();
        Field[] fields = searchCriteria.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object searchValue = field.get(searchCriteria);

            if (Objects.nonNull(searchValue)) {
                Predicate<T> predicate = generatePredicate(field.getName(), searchValue, objectClass);
                predicates.add(predicate);
            }
        }

        return predicates;
    }

    public LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static <T> void sortList(List<T> list, Enum<?> sortCriteria, boolean ascendingSort) {
        if (sortCriteria != null && !list.isEmpty()) {
            String methodName = "get" + sortCriteria.name().toLowerCase();
            try {
                Method method = Arrays.stream(list.get(0).getClass().getMethods())
                                      .filter(m -> m.getName().equalsIgnoreCase(methodName))
                                      .findFirst()
                                      .orElse(null);

                if (method != null) {
                    Comparator<T> comparator = Comparator.comparing(t -> {
                        try {
                            return (Comparable) method.invoke(t);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
                    if (!ascendingSort) {
                        comparator = comparator.reversed();
                    }
                    list.sort(comparator);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static <T> Predicate<T> generatePredicate(String fieldName, Object searchValue, Class<T> objectClass) {
        return object -> {
            try {
                Field field = objectClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object fieldValue = field.get(object);

                if (fieldValue instanceof String) {
                    return ((String) fieldValue).contains((String) searchValue);
                } else {
                    return Objects.equals(fieldValue, searchValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        };
    }

    public static <T, R extends Comparable<R>> Comparator<T> toSort(Function<T, R> keyExtractor, boolean ascendingSort) {
        return ascendingSort ? Comparator.comparing(keyExtractor) : Comparator.comparing(keyExtractor).reversed();
    }
}
