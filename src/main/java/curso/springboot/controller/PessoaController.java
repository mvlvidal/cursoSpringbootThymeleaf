package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj",new Pessoa());
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoas",pessoasIt);
		return modelAndView;

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
			modelAndView.addObject("pessoas",pessoasIt);
			modelAndView.addObject("pessoaobj",new Pessoa());
			
			List<String> msg = new ArrayList<String>();
			
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage()); //Vem das anotations @NotEmpty / @NotNull
			}
			
			modelAndView.addObject("msg", msg);
			
			return modelAndView;
		}
		
		if(pessoa != null) {
			pessoaRepository.save(pessoa);
		}

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoas",pessoasIt);
		modelAndView.addObject("pessoaobj",new Pessoa());
		
		return modelAndView;
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listarpessoas")
	public ModelAndView listar() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoaobj",new Pessoa());
		modelAndView.addObject("pessoas",pessoasIt);
		
		return modelAndView;
		
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa){
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		modelAndView.addObject("pessoaobj", pessoa.get());
		
		return modelAndView;
		
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa){
		
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas",pessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;
		
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomePesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoas",pessoaRepository.findByNome(nomePesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa){
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		
		Pessoa pessoa = pessoaRepository.findById(idpessoa).get();
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", pessoa.getTelefones());
		
		
		return modelAndView;
		
	}
	
	@PostMapping("**/addfonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone,@PathVariable("pessoaid") Long pessoaid) {
				
		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if(telefone != null){
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", pessoa.getTelefones());
			
			List<String> msg = new ArrayList<String>();
			
			if(telefone.getNumero().isEmpty() || telefone.getNumero() == null) {
				msg.add("Numero deve ser informado.");
			}
			
			if(telefone.getTipo().isEmpty() || telefone.getTipo() == null) {
				msg.add("Tipo deve ser informado.");
			}
			
			modelAndView.addObject("msg",msg);
			
			return modelAndView;
		}
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		
		
		
		modelAndView.addObject("pessoaobj", pessoa);
		
		telefone.setPessoa(pessoa);	
		
		if(telefone != null && pessoa != null) {
			telefoneRepository.save(telefone);
		}
		
		modelAndView.addObject("telefones", pessoa.getTelefones());
		
		return modelAndView;
		
	}
	
	@GetMapping("**/removertelefone/{idtelefone}")
	public ModelAndView excluirTelefone(@PathVariable("idtelefone") Long idtelefone){
		
		Telefone telefone = telefoneRepository.findById(idtelefone).get();
		telefoneRepository.deleteById(telefone.getId());
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		
		modelAndView.addObject("telefones",telefone.getPessoa().getTelefones());
		
		modelAndView.addObject("pessoaobj", telefone.getPessoa());

		return modelAndView;
		
	}

}
