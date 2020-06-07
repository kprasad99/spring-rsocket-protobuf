package io.github.kprasad99.rsocket.orm.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Person {

	@Id
	private int Id;
	private String firstName;
	private String lastName;
	private int age;
}
