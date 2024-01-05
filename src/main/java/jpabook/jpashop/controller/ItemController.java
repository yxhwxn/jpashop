package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * Model 객체를 통해 form 이라는 이름으로 'BookForm' 객체를 뷰로 전달
     * 이렇게 함으로써 items/new" 경로로 들어온 사용자에게 createItemForm 뷰 페이지에서 BookForm 객체를 사용하여 사용자에게 입력을 받을 수 있게 됨
     */
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    // 상품 등록
    @PostMapping("items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";   // summit 버튼을 누르면 상품 목록 조회로 redirection
    }
}
