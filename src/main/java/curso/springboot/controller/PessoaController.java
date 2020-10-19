package curso.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj",new Pessoa());
		
		return modelAndView;

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {
		
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
	public ModelAndView pesquisar(@RequestParam("nomePesquisa") String nomePesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoas",pessoaRepository.findByNome(nomePesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}

}
