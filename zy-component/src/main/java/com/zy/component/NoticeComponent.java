package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Article;
import com.zy.entity.cms.Notice;
import com.zy.util.GcUtils;
import com.zy.vo.*;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.getThumbnail;

@Component
public class NoticeComponent {

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日";

//	public ArticleAdminVo buildAdminVo(Article article, boolean withDetail) {
//		ArticleAdminVo articleAdminVo = new ArticleAdminVo();
//		BeanUtils.copyProperties(article, articleAdminVo, "detail");
//		if (withDetail) {
//			articleAdminVo.setContent(article.getContent());
//		}
//		articleAdminVo.setImageBig(getThumbnail(article.getImage(), 750, 450));
//		articleAdminVo.setImageThumbnail(getThumbnail(article.getImage(), 300, 180));
//		articleAdminVo.setCreatedTimeLabel(GcUtils.formatDate(article.getCreatedTime(), TIME_PATTERN));
//		articleAdminVo.setReleasedTimeLabel(GcUtils.formatDate(article.getCreatedTime(), TIME_PATTERN));
//		return articleAdminVo;
//	}

	public NoticeListVo buildListVo(Notice notice ) {
		NoticeListVo noticeListVo = new NoticeListVo();
		BeanUtils.copyProperties(notice, noticeListVo);
		noticeListVo.setCreatedTimeLabel(GcUtils.formatDate(notice.getCreatedTime(), TIME_PATTERN));
		return noticeListVo;
	}

	public NoticeDetailVo buildDetailVo(Notice notice ) {
		NoticeDetailVo noticeDetailVo = new NoticeDetailVo();
		BeanUtils.copyProperties(notice, noticeDetailVo);
		noticeDetailVo.setCreatedTimeLabel(GcUtils.formatDate(notice.getCreatedTime(), TIME_PATTERN));
		return noticeDetailVo;
	}
}
