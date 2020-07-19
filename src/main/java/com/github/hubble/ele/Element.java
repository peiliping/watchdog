package com.github.hubble.ele;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;


@Getter
@ToString
@AllArgsConstructor
public abstract class Element implements Comparable<Element> {


    protected final long id;


    @Override public int compareTo(@NotNull Element that) {

        return Long.compare(this.id, that.id);
    }


    public abstract boolean diff(Element other);


    public boolean same(Element other) {

        return !diff(other);
    }
}
