package curso.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.models.Telefones;
import curso.springboot.models.Usuarios;
import curso.springboot.repository.ProfissaoRepository;
import curso.springboot.repository.TelefoneRepository;
import curso.springboot.repository.UsuarioRepository;

@Controller
public class UsuarioController {
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private TelefoneRepository trepository;
	@Autowired
	private ReportUtil report;
	@Autowired
	private ProfissaoRepository prepository;
	@RequestMapping(method = RequestMethod.GET, value = "/cadastrousuario")
	public String inicio() {
		ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
		model.addObject("usuarioobj", new Usuarios());
		return "cadastro/cadastrousuario";
	}
	@GetMapping("**/baixarcurriculo/{idusuario}")
	public void baixarCurriculo(@PathVariable("idusuario")Long idusuario,HttpServletResponse response, HttpServletRequest request) throws IOException {
		Usuarios u = repository.findById(idusuario).get();
		if(u.getCurriculo() != null) {
			response.setContentLength(u.getCurriculo().length);
			response.setContentType(u.getTipoFileCurriculo());
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", u.getNomeFileCurriculo());
			response.setHeader(headerKey, headerValue);
			response.getOutputStream().write(u.getCurriculo());
		}
	}
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarusuario", consumes = {"multipart/form-data"})
	public ModelAndView salvar(@Valid Usuarios u, BindingResult binding, final MultipartFile file) {
		u.setTelefones(trepository.findByUsuario(u.getId()));
		if (binding.hasErrors()) {
			ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
			Iterable<Usuarios> usuariosIt = repository.findAll();
			model.addObject("usuarios", usuariosIt);
			model.addObject("usuarioobj", u);
			List<String> msg = new ArrayList<String>();
			for (ObjectError objecterror : binding.getAllErrors()) {
				msg.add(objecterror.getDefaultMessage());
			}
			model.addObject("msg", msg);
			return model;
		}
		if(file.getSize() > 0) {
			try {
				u.setCurriculo(file.getBytes());
				u.setNomeFileCurriculo(file.getOriginalFilename());
				u.setTipoFileCurriculo(file.getContentType());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			if(u.getId() != null && u.getId() > 0) {
				Usuarios usuariotemp =  repository.findById(u.getId()).get();
				u.setCurriculo(usuariotemp.getCurriculo());
				u.setNomeFileCurriculo(usuariotemp.getNomeFileCurriculo());
				u.setTipoFileCurriculo(usuariotemp.getTipoFileCurriculo());
			}
		}
		
		repository.save(u);
		ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
		model.addObject("usuarios", repository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		model.addObject("usuarioobj", new Usuarios());
		model.addObject("profissoes",prepository.findAll());
		return model;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listarusuario")
	public ModelAndView usuarios() {
		ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
		model.addObject("usuarioobj", new Usuarios());
		model.addObject("usuarios", repository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		model.addObject("profissoes",prepository.findAll());
		return model;
	}

	@GetMapping("/editarusuario/{idusuario}")
	public ModelAndView editar(@PathVariable("idusuario") Long idusuario) {
		ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
		Optional<Usuarios> usuario = repository.findById(idusuario);
		model.addObject("usuarioobj", usuario.get());
		model.addObject("profissoes",prepository.findAll());
		return model;
	}

	@GetMapping("/telefonesusuario/{idusuario}")
	public ModelAndView telefones(@PathVariable("idusuario") Long idusuario) {
		Optional<Usuarios> usuario = repository.findById(idusuario);
		ModelAndView model = new ModelAndView("cadastro/telefones");
		model.addObject("usuarioobj", usuario.get());
		model.addObject("telefone", trepository.findByUsuario(idusuario));
		return model;
	}

	@PostMapping("**/pesquisarusuario")
	public ModelAndView pesquisar(@RequestParam("nome") String nome,
			@RequestParam("sexo") String sexo, @PageableDefault(size=5,sort = {"nome"}) Pageable pageable) {
		Page<Usuarios> usuarios = null;
		ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
		if(sexo != null && !sexo.isEmpty()) {
			if(nome !=null && !nome.isEmpty()) {
			 usuarios =repository.findPessoaByNameandSexPage(nome, sexo, pageable);
			 model.addObject("nome", nome);
			 model.addObject("sexo",sexo);
			}else {
				usuarios = repository.findPessoaBySexoPage(sexo, pageable);
				 model.addObject("sexo",sexo);
			}
		}else if(nome !=null && !nome.isEmpty()) {
			usuarios = repository.findPessoaByNamePage(nome, pageable);
			model.addObject("nome", nome);
		}else {
			usuarios = repository.findAll(pageable);
		}
		model.addObject("usuarios", usuarios);
		model.addObject("usuarioobj", new Usuarios());
		model.addObject("profissoes",prepository.findAll());
		return model;
	}

	@GetMapping("/deletarusuario/{idusuario}")
	public ModelAndView excluir(@PathVariable("idusuario") Long idusuario) {
		repository.deleteById(idusuario);
		ModelAndView model = new ModelAndView("cadastro/cadastrousuario");
		model.addObject("usuarios", repository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
		model.addObject("usuarioobj", new Usuarios());
		model.addObject("profissoes",prepository.findAll());
		return model;
	}

	@PostMapping("**/addfonesusuario/{idusuario}")
	public ModelAndView addfone(Telefones telefone, @PathVariable("idusuario") Long idusuario) {
		Usuarios usuario = repository.findById(idusuario).get();
		if (telefone != null
				&& (telefone.getNumero() != null && telefone.getNumero().isEmpty() || telefone.getNumero() == null || telefone.getTipo() !=null && telefone.getTipo().isEmpty() || telefone.getTipo() == null)) {
			ModelAndView model = new ModelAndView("cadastro/telefones");
			model.addObject("usuarioobj", usuario);
			model.addObject("telefone", trepository.findByUsuario(idusuario));
			List<String> msg = new ArrayList<String>();
			if(telefone.getNumero().isEmpty() || telefone.getNumero() == null) {
				msg.add("Número não pode ser nulo");
			}
			if(telefone.getTipo().isEmpty() || telefone.getTipo() == null) {
				msg.add("Tipo não pode ser nulo");
			}
			model.addObject("msg", msg);
			return model;
		}
		telefone.setUsuario(usuario);
		trepository.save(telefone);
		ModelAndView model = new ModelAndView("cadastro/telefones");
		model.addObject("usuarioobj", usuario);
		model.addObject("telefone", trepository.findByUsuario(idusuario));
		return model;
	}

	@GetMapping("/deletartelefone/{idtelefone}")
	public ModelAndView excluirtelefone(@PathVariable("idtelefone") Long idtelefone) {
		Usuarios usuario = trepository.findById(idtelefone).get().getUsuario();
		trepository.deleteById(idtelefone);
		ModelAndView model = new ModelAndView("cadastro/telefones");
		model.addObject("usuarioobj", usuario);
		model.addObject("telefone", trepository.findByUsuario(usuario.getId()));
		return model;
	}
	@GetMapping("/usuariospage")
	public ModelAndView carregarUsuariosPorPaginacao(@PageableDefault(size = 5) Pageable pageable, ModelAndView model, @RequestParam("nome") String nome) {
		Page<Usuarios> pageusuario = repository.findAll(pageable);
		model.addObject("usuarios", pageusuario);
		model.addObject("usuarioobj", new Usuarios());
		model.addObject("profissoes", prepository.findAll());
		model.addObject("nome",nome);
		model.setViewName("cadastro/cadastrousuario");
		return model;
	}
	
	@GetMapping("**/pesquisarusuario")
	public void imprime(@RequestParam("nome") String nome,
			@RequestParam("sexo") String sexo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Usuarios> usuarios = new ArrayList<Usuarios>();
		if(sexo != null && !sexo.isEmpty()) {
			if(nome !=null && !nome.isEmpty()) {
			 usuarios =repository.findUsuarioBySexoAndName(nome, sexo);
			}else {
				usuarios = repository.findbySex(sexo);
			}
		}else if(nome !=null && !nome.isEmpty()) {
			usuarios = repository.findUsuarioByName(nome);
		}else {
			Iterable<Usuarios> iterator =  repository.findAll();
			for(Usuarios usuario : iterator) {
				usuarios.add(usuario);
			}
		}
			byte[] pdf = report.gerarRelatorio(usuarios, "usuarios", request.getServletContext());
			response.setContentLength(pdf.length);
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
			response.setHeader(headerKey, headerValue);
			response.getOutputStream().write(pdf);
	}
}
