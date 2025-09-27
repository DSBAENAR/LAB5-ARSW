package edu.eci.arsw.blueprints.persistence.impl;

import java.util.Objects;

public class Tuple<T1, T2> {

    T1 o1;
    T2 o2;

    /**
     * Constructor for Tuple.
     * @param o1 the first element
     * @param o2 the second element
     */
    public Tuple(T1 o1, T2 o2) {
        super();
        this.o1 = o1;
        this.o2 = o2;
    }

    /**
     * Gets the first element of the tuple.
     * @return T1 the first element
     */
    public T1 getElem1() {
        return o1;
    }

    /**
     * Gets the second element of the tuple.
     * @return T2 the second element
     */
    public T2 getElem2() {
        return o2;
    }

    /**
     * Gets the hash code of the tuple.
     * @return int the hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.o1);
        hash = 17 * hash + Objects.hashCode(this.o2);
        return hash;
    }


    /**
     * Checks if this tuple is equal to another object.
     * @param obj the object to compare
     * @return boolean true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple<?, ?> other = (Tuple<?, ?>) obj;
        if (!Objects.equals(this.o1, other.o1)) {
            return false;
        }
        if (!Objects.equals(this.o2, other.o2)) {
            return false;
        }
        return true;
    }
    
    
}
