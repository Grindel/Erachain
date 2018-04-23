package gui.transaction;

import javax.swing.JLabel;
import javax.swing.JTextField;

import core.item.assets.Order;
import core.transaction.CreateOrderTransaction;
import lang.Lang;
import utils.MenuPopupUtil;

@SuppressWarnings("serial")
public class CreateOrderDetailsFrame extends Rec_DetailsFrame
{
	public CreateOrderDetailsFrame(CreateOrderTransaction orderCreation)
	{
		super(orderCreation);

		Order order = orderCreation.getOrder();

		//LABEL HAVE
		++labelGBC.gridy;
		JLabel haveLabel = new JLabel(Lang.getInstance().translate("Have") + ":");
		this.add(haveLabel, labelGBC);

		//HAVE
		++detailGBC.gridy;
		JTextField have = new JTextField(
				order.getAmountHave().toPlainString() + " x "
						+ String.valueOf(order.getHaveAsset().toString()));
		have.setEditable(false);
		MenuPopupUtil.installContextMenu(have);
		this.add(have, detailGBC);

		//LABEL WANT
		++labelGBC.gridy;
		JLabel wantLabel = new JLabel(Lang.getInstance().translate("Want") + ":");
		this.add(wantLabel, labelGBC);

		//HAVE
		++detailGBC.gridy;
		JTextField want = new JTextField(
				order.getAmountWant().toPlainString() + " x "
						+ String.valueOf(order.getWantAsset().toString()));
		want.setEditable(false);
		MenuPopupUtil.installContextMenu(want);
		this.add(want, detailGBC);

		//LABEL PRICE
		++labelGBC.gridy;
		JLabel priceLabel = new JLabel(Lang.getInstance().translate("Price") + ":");
		this.add(priceLabel, labelGBC);

		//PRICE
		++detailGBC.gridy;
		JTextField price = new JTextField(order.getPrice().toPlainString()); // + " / " + order.getPriceCalcReverse().toPlainString());
		price.setEditable(false);
		MenuPopupUtil.installContextMenu(price);
		this.add(price, detailGBC);

		//PACK
		//		this.pack();
		//        this.setResizable(false);
		//       this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
