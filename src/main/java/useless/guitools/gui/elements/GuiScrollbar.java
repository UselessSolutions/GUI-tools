package useless.guitools.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;

public class GuiScrollbar extends GuiButton implements IGuiScrollable, IGuiButtonSound, IGuiRect {
	public float scrollAreaHeight;
	private float scrollAmount = 0;
	private boolean isHeld = false;
	private int barPos = 0;
	public GuiScrollbar(int id, int xPosition, int yPosition, int width, int height, float scrollAreaHeight){
        super(id, xPosition, yPosition, width, height, "");
        this.scrollAreaHeight = scrollAreaHeight;
    }
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		mouseDragged(mc, mouseX, mouseY);
		if (getScrollBarPercentage() > 1.0f) {
			return;
		}
		GL11.glDisable(3553);
		int scrollBarHeightPx = getScrollBarHeightPixels();
		int scrollBarY = getScrollBarY();
		int scrollColorSecondary = 0xC0C0C0;
		int scrollColorPrimary = 0x808080;
		if (isHeld){
			scrollColorSecondary = 0xA0A0C0;
			scrollColorPrimary = 0x606080;
		}
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setColorOpaque(0, 0, 0);
		t.drawRectangle(xPosition, yPosition, 6, height);
		t.setColorRGBA_I(scrollColorPrimary, 255);
		t.drawRectangle(xPosition, scrollBarY, 6, scrollBarHeightPx);
		t.setColorRGBA_I(scrollColorSecondary, 255);
		t.drawRectangle(xPosition + 1, scrollBarY, 5, scrollBarHeightPx - 1);
		t.draw();
		GL11.glEnable(3553);
	}
	private int getScrollBarHeightPixels(){
		int scrollBarHeightPx = (int) ((height / scrollAreaHeight) * height);
		if (scrollBarHeightPx < 32) {
			scrollBarHeightPx = 32;
		}
		return scrollBarHeightPx;
	}
	private int getScrollBarY(){
		float scrollPercent = scrollAmount / (scrollAreaHeight - height);
        return (int)((float)yPosition + (float)(height - getScrollBarHeightPixels()) * scrollPercent);
	}
	public float getScrollBarPercentage(){
		return height / scrollAreaHeight;
	}
	public void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
		if (isHeld){
			scrollAmount = ((float) (mouseY - barPos - yPosition) /height) * scrollAreaHeight;
			onScroll();
		}
	}
	public void mouseReleased(int mouseX, int mouseY) {
		isHeld = false;
	}

	public boolean mouseClicked(Minecraft mc, int mouseX, int mouseY) {
		if (super.mouseClicked(mc, mouseX, mouseY)){
			if (!isHeld){
				isHeld = true;
				int scrollBarHeightPx = getScrollBarHeightPixels();
				int scrollBarY = getScrollBarY();
				if (mouseY > scrollBarY && mouseY < scrollBarY + scrollBarHeightPx){
					barPos = mouseY - scrollBarY;
				} else {
					barPos = scrollBarHeightPx/2;
				}
			}
			return true;
		}
		return false;
	}
	@Override
	public void scroll(int mouseX, int mouseY,float amount) {
		if (amount == 0.0f || !isHovered(mouseX, mouseY)) {
			return;
		}
		this.scrollAmount += amount;
		this.onScroll();
	}
	@Override
	public void onScroll() {
		if (this.scrollAmount < 0.0f || height > scrollAreaHeight) {
			this.scrollAmount = 0.0f;
		} else if (this.scrollAmount > (scrollAreaHeight - height)) {
			this.scrollAmount = scrollAreaHeight - height;
		}
	}
	public void setScrollAreaHeight(float scrollAreaHeight){
		this.scrollAreaHeight = scrollAreaHeight;
		onScroll();
	}
	public float getScrollAreaHeight(){
		return scrollAreaHeight;
	}
	public float getScrollAmount(){
		return scrollAmount;
	}
	public void setScrollAmount(float scrollAmount){
		this.scrollAmount = scrollAmount;
	}

	@Override
	public boolean canPlaySound(int mouseX, int mouseY) {
		return false;
	}

	@Override
	public String getSoundPath(int mouseX, int mouseY) {
		return null;
	}
}
