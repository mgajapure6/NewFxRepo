package app.db.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Provider")
public class Provider {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer providerId;

	@Column(length = 100)
	private String providerName;

	@OneToOne
	@JoinColumn(name = "providerId")
	private User user;

	@OneToMany(mappedBy = "provider")
	private Set<ProviderProduct> providerProducts;

	public Provider(Integer providerId, String providerName) {
		super();
		this.providerId = providerId;
		this.providerName = providerName;
	}

	public Provider() {
		super();
	}

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Provider [providerId=" + providerId + ", providerName=" + providerName + ", user=" + user
				+ ", providerProducts=" + providerProducts + "]";
	}

}
