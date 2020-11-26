package com.moyisuiying.booksystem.service.impl;

import com.moyisuiying.booksystem.entity.Book;
import com.moyisuiying.booksystem.mapper.BookMapper;
import com.moyisuiying.booksystem.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 书籍表 服务实现类
 * </p>
 *
 * @author 陌意随影
 * @since 2020-11-26
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

}
