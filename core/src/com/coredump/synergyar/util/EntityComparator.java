package com.coredump.synergyar.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;

import java.util.Comparator;

/**
 * Created by fabio on 12/9/15.
 */
public abstract class EntityComparator implements Comparator<Entity> {
    private SortedIteratingSystem mSystem;

    public EntityComparator(SortedIteratingSystem system) {
        super();
        mSystem = system;
    }
}
