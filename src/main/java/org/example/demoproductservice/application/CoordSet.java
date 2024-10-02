package org.example.demoproductservice.application;

import java.util.List;

public record CoordSet(List<CoordItem> items, Long totalCost) {
}
