package joot.m2.client.util;

import com.badlogic.gdx.Gdx;
import com.google.gwt.user.client.ui.*;

/**
 * 弹窗工具类
 * 
 * @author linxing
 *
 */
public final class DialogUtil {
	
	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}
	
	/**
	 * 警告信息
	 * 
	 * @param title 标题
	 * @param message 信息内容
	 * @param closed 窗体关闭后的操作
	 */
	public static void alert(String title, String message, OperationConsumer closed) {
		new AlertDialogBox(title, message, closed).show();
	}

	
	/**
	 * 确认信息
	 * 
	 * @param title 标题
	 * @param message 信息内容
	 * @param ok 点击“确定”按钮后执行的操作
	 */
	public static void confirm(String title, String message, OperationConsumer ok) {
		new ConfirmDialogBox(title, message, ok).show();
	}

	private static class ConfirmDialogBox extends DialogBox {
		private OperationConsumer onOK;
		private ConfirmDialogBox(String title, String message, OperationConsumer onOK) {
			this.onOK = onOK;

			showRelativeTo(RootPanel.get());

			// Set the dialog box's caption.
			setText(title);

			VerticalPanel vPanel = new VerticalPanel();
			HorizontalPanel hPanel = new HorizontalPanel();

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			// Center this bad boy.
			center();

			vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

			Button ok = new Button("确定");
			ok.addClickHandler(event -> ConfirmDialogBox.this.onPositive());

			Button cancel = new Button("取消");
			cancel.addClickHandler(event -> ConfirmDialogBox.this.onNegative());

			hPanel.add(ok);
			hPanel.add(cancel);

			Label lbl = new Label(message);
			vPanel.add(lbl);
			vPanel.add(hPanel);

			setWidget(vPanel);
		}

		protected void onPositive () {
			if (onOK != null) {
				onOK.op();
			}
			this.hide();
		}

		protected void onNegative () {
			this.hide();
		}
	}

	private static class AlertDialogBox extends DialogBox {
		private OperationConsumer onOK;
		private AlertDialogBox(String title, String message, OperationConsumer onOK) {
			this.onOK = onOK;

			showRelativeTo(RootPanel.get());

			// Set the dialog box's caption.
			setText(title);

			VerticalPanel vPanel = new VerticalPanel();
			HorizontalPanel hPanel = new HorizontalPanel();

			// Enable animation.
			setAnimationEnabled(true);

			// Enable glass background.
			setGlassEnabled(true);

			// Center this bad boy.
			center();

			vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

			Button ok = new Button("确定");
			ok.addClickHandler(event -> AlertDialogBox.this.onPositive());

			hPanel.add(ok);

			Label lbl = new Label(message);
			vPanel.add(lbl);
			vPanel.add(hPanel);

			setWidget(vPanel);
		}

		protected void onPositive () {
			if (onOK != null) {
				onOK.op();
			}
			this.hide();
		}
	}
}
