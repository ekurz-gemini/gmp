package edu.gemini.aspen.gmp.pcs.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * A class that holds an update for the PCS system.
 */
public final class PcsUpdate {

    /**
     * List of zernikes to be sent
     */
    private final List<Double> _zernikes;

    /**
     * Constructor
     */
    public PcsUpdate(Double[] values) {
        _zernikes = ImmutableList.copyOf(values);
    }

    /**
     * Get the list of zernikes as an array of doubles.
     * @return an array of doubles with the zernikes
     * stored in this update
     */
    public Double[] getZernikes() {
        Double[] results = new Double[_zernikes.size()];
         _zernikes.toArray(results);
        return results;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PcsUpdate pcsUpdate = (PcsUpdate) o;

        if (_zernikes != null ? !_zernikes.equals(pcsUpdate._zernikes) : pcsUpdate._zernikes != null) return false;
        //everything matches
        return true;
    }

    @Override
    public int hashCode() {
        return _zernikes != null ? _zernikes.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PcsUpdate{" +
               "zernikes=" + _zernikes +
               '}';
    }
}
