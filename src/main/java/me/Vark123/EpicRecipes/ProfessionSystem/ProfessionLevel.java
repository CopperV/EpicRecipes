package me.Vark123.EpicRecipes.ProfessionSystem;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableObject;

import lombok.Getter;

@Getter
public enum ProfessionLevel {

	LAIK(0, "Laik", 0),
	POCZATKUJACY(1, "Poczatkujacy", 10),
	PRAKTYKUJACY(2, "Praktykujacy", 30),
	CZELADNIK(3, "Czeladnik", 70),
	ZAWODOWIEC(4, "Zawodowiec", 150),
	EKSPERT(5, "Ekspert", 310),
	KORYFEUSZ(6, "Koryfeusz", 630),
	INZYNIUER(7, "Inzynier", 1270),
	MAGISTER(8, "Magister", 2550),
	MISTRZ(9, "Mistrz", 5110),
	ARCYMISTRZ(10, "Arcymistrz", 10230),
	GURU(11, "Guru", 20470),
	LEGENDA(12, "Legenda", 40950);
	
	private int id;
	private String display;
	private int amount;
	
	private ProfessionLevel(int id, String display, int amount) {
		this.id = id;
		this.display = display;
		this.amount = amount;
	}
	
	public static Optional<ProfessionLevel> getById(int level) {
		MutableObject<Optional<ProfessionLevel>> result = new MutableObject<>(Optional.empty());
		Arrays.asList(values()).stream().filter(prof -> {
			return prof.id == level;
		}).findFirst().ifPresent(prof -> {
			result.setValue(Optional.of(prof));
		});
		return result.getValue();
	}
	
}
