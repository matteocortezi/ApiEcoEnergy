package eco.energy.api.repository;
import eco.energy.api.model.Dispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {
}
