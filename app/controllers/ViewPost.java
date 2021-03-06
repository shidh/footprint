package controllers;

import models.Comment;
import models.Post;
import models.User;
import play.mvc.Controller;

public class ViewPost extends Controller {
	public static void page(Long postId) {
		if (postId != null) {
			Post post = Post.findById(postId);
			if (post != null) {
				boolean showEditButton = false;
				User user = null;

				String userId = session.get("userId");
				if (userId != null) {
					user = User.findById(Long.parseLong(userId));
					showEditButton = user != null;
				}

				render(post, user, showEditButton);
			}
		}

		// wrong post id or no post id provided
		MyPosts.page();
	}

	public static void postComment(Long postId, String commentText,
			Integer commentRating) {
		if (postId != null) {
			Post post = Post.findById(postId);

			if (post != null) {
				String userId = session.get("userId");
				if (userId != null) {
					User sender = User.findById(Long.parseLong(userId));

					if (sender != null) {
						Comment comment = new Comment(commentText,
								commentRating, sender, post);
						comment.save();

						post.refresh();
						post.rating = new Double(post.rating.doubleValue()
								+ commentRating.intValue());
						post.save();
					}
				}
			}
		}

		ViewPost.page(postId);
	}
}
