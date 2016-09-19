package com.zy.component;

import static com.zy.util.GcUtils.getThumbnail;

import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Article;
import com.zy.util.GcUtils;
import com.zy.vo.ArticleAdminVo;
import com.zy.vo.ArticleDetailVo;
import com.zy.vo.ArticleListVo;

@Component
public class ArticleComponent {

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日";

	public ArticleAdminVo buildAdminVo(Article article, boolean withDetail) {
		ArticleAdminVo articleAdminVo = new ArticleAdminVo();
		BeanUtils.copyProperties(article, articleAdminVo, "detail");
		if (withDetail) {
			articleAdminVo.setContent(article.getContent());
		}
		articleAdminVo.setImageBig(getThumbnail(article.getImage(), 750, 450));
		articleAdminVo.setImageThumbnail(getThumbnail(article.getImage(), 300, 180));
		articleAdminVo.setCreatedTimeLabel(GcUtils.formatDate(article.getCreatedTime(), TIME_PATTERN));
		articleAdminVo.setReleasedTimeLabel(GcUtils.formatDate(article.getCreatedTime(), TIME_PATTERN));
		return articleAdminVo;
	}
	
	public ArticleDetailVo buildDetailVo(Article article) {
		ArticleDetailVo articleDetailVo = new ArticleDetailVo();
		BeanUtils.copyProperties(article, articleDetailVo);
		articleDetailVo.setImageBig(getThumbnail(article.getImage(), 750, 450));
		articleDetailVo.setImageThumbnail(getThumbnail(article.getImage(), 300, 180));
		articleDetailVo.setReleasedTimeLabel(GcUtils.formatDate(article.getReleasedTime(), SIMPLE_TIME_PATTERN));
		return articleDetailVo;
	}
	
	public ArticleListVo buildListVo(Article article) {
		ArticleListVo articleListVo = new ArticleListVo();
		BeanUtils.copyProperties(article, articleListVo);
		articleListVo.setImageThumbnail(getThumbnail(article.getImage(), 300, 180));
		articleListVo.setReleasedTimeLabel(GcUtils.formatDate(article.getReleasedTime(), SIMPLE_TIME_PATTERN));
		return articleListVo;
	}
}
