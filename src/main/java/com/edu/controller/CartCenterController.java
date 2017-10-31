package com.edu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.utils.WxUserOAuthHelper;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.dao.StudentRepository;
import com.edu.domain.DerivedProduct;
import com.edu.domain.ImageCollection;
import com.edu.domain.Product;
import com.edu.domain.ProductContainer;
import com.edu.domain.Student;

import me.chanjar.weixin.mp.api.WxMpService;

@Controller
public class CartCenterController {
	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private StudentRepository repository;

	@Autowired
	private WxUserOAuthHelper oauthHelper;

    public final static String CART_PATH = "/user/cart";
    public final static String CART_CALLBACK_PATH = "/user/cart/cb";

    public final static String SESSION_OPENID_KEY = "openCode";

    @GetMapping(CART_PATH)
    public String cart(HttpServletRequest request, HttpSession session, Model model) {
        Object openIdInSession = session.getAttribute(SESSION_OPENID_KEY);

        if (openIdInSession == null) { // OAuth to get OpenID
            return oauthHelper.buildOAuth2RedirectURL(request, CART_PATH, CART_CALLBACK_PATH);
        } else {
            return doShowCart((String) openIdInSession, model);
        }
    }

    @GetMapping(CART_CALLBACK_PATH)
    public String cartCallback(@RequestParam(value="code") String authCode, Model model, HttpSession session) {
        String openId = null;

        try {
            openId = oauthHelper.getOpenIdWhenOAuth2CalledBack(authCode, session);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return "error_500";
        }

        return doShowCart(openId, model);
    }

	private String doShowCart(String openId, Model model) {
        if (openId == null) {
            return "error_500";
        }
		
		Student student = repository.findOneByOpenCode(openId);
		if (student == null) {
			Student newStudent = new Student();
			newStudent.setOpenCode(openId);
			model.addAttribute("student", newStudent);
			return "user_signup";
		} else {
			Set<Product> products = student.getCart().getProducts();
			Set<DerivedProduct> derivedProducts = student.getCart().getDerivedProducts();
			Set<ImageCollection> imageCollection = student.getCart().getImageCollection();

			Stream<ProductContainer> productsStream = products.stream()
					.map(x -> new ProductContainer(x.getProductName(), x.getProductCategory().getCategoryName(),
							x.getProductPrice(), x.getProductDescription(),
							"/Images/" + x.getProductImages().stream().findFirst().get().getId() + "/thumbnail", 1,
							x.getId(), 1));
			Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
					.map(x -> new ProductContainer(x.getProduct().getProductName(), x.getProduct().getProductCategory().getCategoryName(),
							x.getProduct().getProductPrice(), x.getProduct().getProductDescription(),
							"/Images/" + x.getImage().getId() + "/thumbnail", 1, x.getId(), 2));
			Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
					.map(x -> new ProductContainer(x.getCollectionName(), "作品集", x.getPrice(),
							x.getCollectionDescription(),
							"/Images/" + x.getImageCollection().stream().findFirst().get().getId() + "/thumbnail", 1,
							x.getId(), 3));
			model.addAttribute("products", Stream.of(productsStream, derivedProductsStream, imageCollectionStream)
					.flatMap(i -> i).collect(Collectors.toCollection(ArrayList::new)));
			model.addAttribute("code", openId);
			return "user_cart";
		}
	}

	@GetMapping("/user/cartcontent")
	@ResponseBody
	public List<ProductContainer> getCartContent(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		if (null == openCodeObject) {
			return new ArrayList<>();
		}

		String authCode = openCodeObject.toString();
		Student student = repository.findOneByOpenCode(authCode);
		Set<Product> products = student.getCart().getProducts();
		Set<DerivedProduct> derivedProducts = student.getCart().getDerivedProducts();
		Set<ImageCollection> imageCollection = student.getCart().getImageCollection();

		Stream<ProductContainer> productsStream = products.stream()
				.map(x -> new ProductContainer(x.getProductName(), x.getProductCategory().getCategoryName(),
						x.getProductPrice(), x.getProductDescription(),
						"/Images/" + x.getProductImages().stream().findFirst().get().getId() + "/thumbnail", 1,
						x.getId(), 1));
		Stream<ProductContainer> derivedProductsStream = derivedProducts.stream()
				.map(x -> new ProductContainer(x.getProduct().getProductName(), "衍生品", x.getProduct().getProductPrice(),
						x.getProduct().getProductDescription(), "/Images/" + x.getImage().getId() + "/thumbnail", 1,
						x.getId(), 2));
		Stream<ProductContainer> imageCollectionStream = imageCollection.stream()
				.map(x -> new ProductContainer(x.getCollectionName(), "作品集", x.getPrice(), x.getCollectionDescription(),
						"/Images/" + x.getImageCollection().stream().findFirst().get().getId() + "/thumbnail", 1,
						x.getId(), 3));

		return Stream.of(productsStream, derivedProductsStream, imageCollectionStream).flatMap(i -> i)
				.collect(Collectors.toCollection(ArrayList::new));
	}
}
