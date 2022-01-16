package com.bookpie.shop.service;

import com.bookpie.shop.config.ApiConfig;
import com.bookpie.shop.domain.Book;
import com.bookpie.shop.domain.BookCategory;
import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import com.bookpie.shop.domain.dto.book.BookDto;
import com.bookpie.shop.repository.BookCategoryRepository;
import com.bookpie.shop.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;
    @Autowired
    private ApiConfig apiConfig;

    // 카테고리 추가
    public String addCategory(BookCategoryDto dto) {
        BookCategory parentCategory = null;
        if (dto.getParentId() != null) {
            parentCategory = bookCategoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 부모 카테고리는 존재하지 않습니다."));
        }
        BookCategory subCategory = BookCategory.createCategory(dto, parentCategory);
        bookCategoryRepository.save(subCategory);

        return dto.getCategoryName() + " 카테고리 추가";
    }

    // 카테고리 삭제
    public String deleteCategory(Long category_id) {
        BookCategory bookCategory = bookCategoryRepository.findById(category_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리는 존재하지 않습니다."));

        bookCategoryRepository.delete(bookCategory);
        return bookCategory.getCategoryName() + " 카테고리가 삭제되었습니다.";
    }

    // 카테고리 조회
    public List<BookCategoryDto> getAll() {
        return bookCategoryRepository.findAllCategory()
                .stream().map(category -> BookCategoryDto.createDto(category))
                .collect(Collectors.toList());
    }

    // 도서 추가 (임시용)
    public BookDto create(BookDto dto) {
        Book book = Book.createBook(dto);
        Book book1 = bookRepository.save(book);
        return BookDto.createDto(book1);
    }

    // 도서 삭제 (임시용)
    public String delete(Long book_id) {
        Book book = bookRepository.findById(book_id).
                orElseThrow(() -> new IllegalArgumentException("해당 도서는 존재하지 않습니다."));
        bookRepository.delete(book);
        return book.getTitle() + " 도서가 삭제되었습니다.";
    }

    // 알라딘 api 베스트 셀러 조회
    public JSONObject best(String page, String size) {
        int realPage = 1;
        int realSize = 8;
        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        String uri = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey="+apiConfig.getAladinAPI()+"&QueryType=Bestseller" +
                "&MaxResults="+realSize+"&start="+realPage+"&SearchTarget=Book&output=js&Version=20131101";

        return callApi(uri);
    }

    // 도서 상세 조회
    public JSONObject bookDetail(String isbn13, String isbn) {
        String itemType = "ISBN13";
        String realISBN = isbn13;
        if (isbn13 == null) {
            itemType = "ISBN";
            realISBN = isbn;
        }

        String uri = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey="+apiConfig.getAladinAPI()+"&itemIdType="+itemType+"&ItemId="+realISBN+"" +
                "&output=js&Version=20131101&OptResult=authors,reviewList,fulldescription";

        return callApi(uri);
    }

    // 키워드로 검색색
    public JSONObject searchKeyword(String queryType, String keyword, String page, String size) {
        int realPage = 1;
        int realSize = 16;
        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        String uri = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey="+apiConfig.getAladinAPI()+"&Query="+keyword+"&QueryType="+queryType+"" +
                "&MaxResults="+realSize+"&start="+realPage+"&SearchTarget=Book&output=js&Version=20131101";

        return callApi(uri);
    }

    // 카테고리로 조회
    public JSONObject byCategory(Long category_id, String page, String size) {
        int realPage = 1;
        int realSize = 16;
        if (page != null) realPage = Integer.parseInt(page);
        if (size != null) realSize = Integer.parseInt(size);

        String uri = "http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey="+apiConfig.getAladinAPI()+"&QueryType=ItemEditorChoice" +
                "&MaxResults="+realSize+"&start="+realPage+"&SearchTarget=Book&CategoryId="+category_id+"&output=js&Version=20131101";

        return callApi(uri);
    }

    // 추천 도서 (정보나루 api)
    public JSONArray recommend(Long isbn) {
        String uri = "http://data4library.kr/api/recommandList?authKey="+apiConfig.getJungboAPI()+"&isbn13="+isbn+"&format=json";
        JSONObject object = null;
        JSONArray jsonArray = new JSONArray();
        String line = null;

        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            line = br.readLine();

            JSONParser jsonParser = new JSONParser();
            object = (JSONObject) jsonParser.parse(line);

            // 받은 json데이터에서 response 객체를 꺼내고 docs List 생성
            JSONObject response = (JSONObject) object.get("response");
            JSONArray docs = (JSONArray) response.get("docs");

            // 기본 값으로 200개의 데이터를 줌. 10개만 넘길거임
            for (int i = 0; i < 10; i++) {
                jsonArray.add(docs.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    // api 호출 메서드
    private JSONObject callApi(String uri) {
        JSONObject object = null;
        String line = null;

        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            line = br.readLine();

            JSONParser jsonParser = new JSONParser();
            object = (JSONObject) jsonParser.parse(line);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return object;
    }

}

