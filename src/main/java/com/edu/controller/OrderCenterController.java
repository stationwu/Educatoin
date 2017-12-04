package com.edu.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.edu.dao.*;
import com.edu.domain.*;
import com.edu.domain.dto.ProductContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderCenterController {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private CustomerRepository custRepo;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private DerivedProductRepository derivedProductRepository;

	@Autowired
	private ImageCollectionRepository imageCollectionRepository;

	@Autowired
	private ClassProductRepository classProductRepository;

	@Autowired
	private OrderRepository orderRepository;

	public final static String ORDER_LIST_PATH = "/user/orderList";

	public final static String ORDER_DETAIL_PATH = "/order/detail/{id}";

	public final static String SESSION_OPENID_KEY = "openCode";

	//private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/user/order")
	@ResponseBody
	public String createOrder(HttpServletRequest request, @RequestBody List<ProductContainer> products, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");

		String openId = openCodeObject.toString();
		Customer customer = custRepo.findOneByOpenCode(openId);
		Order order = new Order();
		Map<Product, Integer> productMap = new HashMap<>();
		Map<DerivedProduct, Integer> derivedProductMap = new HashMap<>();
		Map<ImageCollection, Integer> imageCollectionMap = new HashMap<>();
		Map<ClassProduct, Integer> classProductMap = new HashMap<>();
		double amount = 0d;
		for (ProductContainer productContainer : products) {
			switch (productContainer.getType()) {
			case 1:
				Product product = productRepository.findOne(productContainer.getId());
				productMap.put(product, productContainer.getQuantity());
				amount += productContainer.getProductPrice() * productContainer.getQuantity();
				customer.getCart().removeProduct(product);
				break;
			case 2:
				DerivedProduct derivedProduct = derivedProductRepository.findOne(productContainer.getId());
				derivedProductMap.put(derivedProduct, productContainer.getQuantity());
				amount += productContainer.getProductPrice() * productContainer.getQuantity();
				customer.getCart().removeDerivedProduct(derivedProduct);
				break;
			case 3:
				ImageCollection imageCollection = imageCollectionRepository.findOne(productContainer.getId());
				imageCollectionMap.put(imageCollection, productContainer.getQuantity());
				amount += productContainer.getProductPrice() * productContainer.getQuantity();
				customer.getCart().removeImageCollection(imageCollection);
				break;
			case 4:
				ClassProduct classProduct = classProductRepository.findOne(productContainer.getId());
				classProductMap.put(classProduct, productContainer.getQuantity());
				amount += productContainer.getProductPrice() * productContainer.getQuantity();
				//Student student = classProduct.getStudent();
//				student.setClassPeriod(student.getClassPeriod()+classProduct.getProduct().getClassPeriod());
//				student.setLeftPeriods(student.getClassPeriod()-student.getDonePeriods());
//				studentRepository.save(student);
				customer.getCart().removeClassProduct(classProduct);
				break;
			}
		}
		custRepo.save(customer);
		order.setProductsMap(productMap);
		order.setDerivedProductsMap(derivedProductMap);
		order.setImageCollectionMap(imageCollectionMap);
		order.setClassProductsMap(classProductMap);
		order.setTotalAmount(amount);
		LocalDate localDate = LocalDate.now();
		order.setDate(localDate.toString());
		order.setCustomer(customer);
		orderRepository.save(order);
		return "下单成功";
	}

	@GetMapping(ORDER_DETAIL_PATH)
	public String orderDetail(@PathVariable(value = "id") Long id, HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Object openCodeObject = session.getAttribute("openCode");
		String openCode = openCodeObject.toString();
		Order foundOrder = orderRepo.findOne(id);
		if (null == foundOrder) {
			return "error_500";
		} else {
			model.addAttribute("detail", foundOrder);
			model.addAttribute("openCode", openCode);
			return "order_detail";
		}
	}

	@GetMapping(ORDER_LIST_PATH)
	public String orderList(HttpSession session, Model model) {
		String openId = (String)session.getAttribute(SESSION_OPENID_KEY);

		model.addAttribute("code", openId);

		return "user_order";
	}

//	private List<OrderContainer> getOrderList(String openId) {
//		if (null == openId) {
//			return new ArrayList<OrderContainer>();
//		}
//
//		Customer customer = custRepo.findOneByOpenCode(openId);
//		return customer.getOrders().stream()
//				.map(x -> new OrderContainer(x, x.getProductsMap(), x.getDerivedProductsMap(),
//						x.getImageCollectionMap(), x.getClassProductsMap()))
//				.collect(Collectors.toCollection(ArrayList::new));
//	}
}
