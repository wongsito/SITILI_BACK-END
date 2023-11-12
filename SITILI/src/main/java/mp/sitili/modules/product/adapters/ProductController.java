package mp.sitili.modules.product.adapters;

import mp.sitili.modules.category.entities.Category;
import mp.sitili.modules.category.use_cases.methods.CategoryRepository;
import mp.sitili.modules.category.use_cases.service.CategoryService;
import mp.sitili.modules.product.use_cases.dto.ProductDTO;
import mp.sitili.modules.product.use_cases.methods.ProductRepository;
import mp.sitili.modules.product.use_cases.service.ProductService;
import mp.sitili.modules.user.entities.User;
import mp.sitili.modules.user.use_cases.methods.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/listAll")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<ProductDTO>> obtenerTodoProductos() {
        List<ProductDTO> products = productRepository.findAllProducts();

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listUp")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<ProductDTO>> obtenerTodoProductosActivos() {
        List<ProductDTO> products = productRepository.findAllByStatusEquals();

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAllVend")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<ProductDTO>> obtenerTodoProductosxVendedor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        List<ProductDTO> products = productRepository.findAllxVend(sellerEmail);

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listUpVend")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<List<ProductDTO>> obtenerTodoProductosxVendedorUp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        List<ProductDTO> products = productRepository.findAllUpxVend(sellerEmail);

        if(products != null){
            return new ResponseEntity<>(products, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(products, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> guardarProducto(@RequestBody Map<String, Object> productData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sellerEmail = authentication.getName();

        //Falta recivir y cargar imagen
        //Lógica

        String name = (String) productData.get("name");
        Integer stock = (Integer) productData.get("stock");
        Double price = (Double) productData.get("price");
        String features = (String) productData.get("features");
        Integer categoryId = (Integer) productData.get("category_id");
        Category category = categoryRepository.getCatById(categoryId);
        User user = userRepository.findById(String.valueOf(sellerEmail)).orElse(null);

        boolean revision =  productService.saveCategory(name, price, stock, features, category, user);

        if(revision){
            return new ResponseEntity<>("Producto creado exitosamente", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Error al guardar producto", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
