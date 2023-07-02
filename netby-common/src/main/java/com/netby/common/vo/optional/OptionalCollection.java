package com.netby.common.vo.optional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * @author: byg
 */
@SuppressWarnings("unused")
public final class OptionalCollection<T> implements Iterable<T> {
    private static final OptionalCollection<?> EMPTY = new OptionalCollection<>();

    private final Collection<T> value;

    private OptionalCollection() {
        this.value = new ArrayList<>();
    }

    public static <T> OptionalCollection<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalCollection<T> t = (OptionalCollection<T>) EMPTY;
        return t;
    }

    private OptionalCollection(Collection<T> value) {
        this.value = Objects.requireNonNull(value);
    }

    public static <T> OptionalCollection<T> of(Collection<T> value) {
        return new OptionalCollection<>(value);
    }

    public static <T> OptionalCollection<T> ofNullable(Collection<T> value) {
        return value == null ? empty() : of(value);
    }

    public Collection<T> get() {
        return value;
    }

    public boolean isNotEmpty() {
        return !value.isEmpty();
    }

    public void isNotEmpty(Consumer<? super Collection<T>> consumer) {
        if (isNotEmpty()) {
            consumer.accept(value);
        }
    }

    public List<T> convertToList() {
        if (isNotEmpty()) {
            return new ArrayList<>(value);
        } else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("unchecked")
    public <R> List<R> convertToList(Class<R> clazz) {
        if (isNotEmpty()) {
            return value.stream().map(t -> (R) t).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }


    public <R> OptionalCollection<R> cast(Class<R> clazz) {
        return OptionalCollection.of(convertToList(clazz));
    }

    public <R> OptionalCollection<R> map(Function<? super T, ? extends R> function) {
        return OptionalCollection.of(convertToList().stream().map(function).collect(Collectors.toList()));
    }

    public Collection<T> orElse(Collection<T> other) {
        return !isNotEmpty() ? value : other;
    }

    public Collection<T> orElseGet(Supplier<? extends Collection<T>> other) {
        return !isNotEmpty() ? value : other.get();
    }

    public <X extends Throwable> Collection<T> orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (!isNotEmpty()) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof OptionalCollection)) {
            return false;
        }

        OptionalCollection<?> other = (OptionalCollection<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return !isNotEmpty()
                ? String.format("OptionalCollection[%s]", value)
                : "OptionalCollection.empty";
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return convertToList().iterator();
    }
}
