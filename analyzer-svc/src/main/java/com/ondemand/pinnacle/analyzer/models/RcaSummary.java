package com.ondemand.pinnacle.analyzer.models;

/**
 * @author Chandu D - i861116
 * @created 13/12/2022 - 4:03 PM
 * @description
 */
public interface RcaSummary<S, C, M, O> {
    O generateRca(S service, C callStack, M rcaStackMap);
}
