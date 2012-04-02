package be.virtualsushi.jfx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Article entity.
 * 
 * @author Pavel Sitnikov (van.frag@gmail.com)
 * 
 */

@Entity
public class Article implements Serializable {

	private static final long serialVersionUID = 3235183180027666349L;

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	private static ResourceBundle validationMessages = ResourceBundle.getBundle("ValidationMessages");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "{code.empty}")
	@Length(max = 50, message = "{code.length}")
	@Column(length = 50, name = "CODE")
	private String code;

	@NotEmpty(message = "{name.empty}")
	@Length(max = 255, message = "{name.length}")
	@Column(length = 255, name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@NotNull(message = "{price.null}")
	@Column(name = "PRICE")
	private Float price;

	@NotNull(message = "{reduction.null}")
	@Column(name = "REDUCTION")
	private Float reduction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getReduction() {
		return reduction;
	}

	public void setReduction(Float reduction) {
		this.reduction = reduction;
	}

	public boolean isNew() {
		return id == null || id == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((reduction == null) ? 0 : reduction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (reduction == null) {
			if (other.reduction != null)
				return false;
		} else if (!reduction.equals(other.reduction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Article [code=" + code + ", name=" + name + "]";
	}

	/**
	 * Validates Article instance according to fields annotations.
	 * Also not allows to reduction be greater than price.
	 * 
	 * @param article
	 * @return
	 */
	public static List<String> validate(Article article) {
		List<String> result = new ArrayList<String>();
		for (ConstraintViolation<Article> violation : validator.validate(article)) {
			result.add(violation.getMessage());
		}
		if (article.getPrice() != null && article.getReduction() != null && article.getReduction() > article.getPrice()) {
			result.add(validationMessages.getString("reduction.too.big"));
		}
		return result;
	}

}
