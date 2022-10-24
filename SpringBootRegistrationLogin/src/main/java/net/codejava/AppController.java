package net.codejava;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
    private TeamRepository teamRepo;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userRepo.save(user);
		
		return "register_success";
	}
	
	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}

    @PostMapping("/create_team")
    public String createTeam(Team team) {


        teamRepo.save(team);
        Team newTeam = teamRepo.findByName(team.getName());
		List<String> names = Arrays.asList(team.getUsers().split(" "));
		User updateUser;
		for(String name: names) {
			updateUser = userRepo.findByFirstName(name);
			updateUser.setTeamId(newTeam.getId());
			userRepo.save(updateUser);
		}
        return "index";
    }

    @GetMapping("/profile")
    public String getTeam(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByEmail(auth.getName());
        model.addAttribute("user", user);
        Optional<Team> team = teamRepo.findById(user.getTeamId());

        model.addAttribute("team", team.get());

        return "profile";
    }

	@GetMapping("/create_team")
	public String createTeamForm(Model model) {
		model.addAttribute("team", new Team());

		return "create_team";
	}
}
