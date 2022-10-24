package net.codejava;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    public Optional<Team> findById(Long id);

    public Team findByName(String name);
}
