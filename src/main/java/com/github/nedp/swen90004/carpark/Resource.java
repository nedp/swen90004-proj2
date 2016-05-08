package com.github.nedp.swen90004.carpark;

/**
 * An interface which supports operations on both ends of a resource.
 */
interface Resource<T> extends ResourceEntry<T>, ResourceExit<T> {}
