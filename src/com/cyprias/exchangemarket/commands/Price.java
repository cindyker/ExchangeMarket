package com.cyprias.exchangemarket.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.cyprias.exchangemarket.Database;
import com.cyprias.exchangemarket.ExchangeMarket;
import com.cyprias.exchangemarket.ItemDb;
import com.cyprias.exchangemarket.Localization;
import com.cyprias.exchangemarket.Utilis.Utils;

public class Price  {

	private ExchangeMarket plugin;

	public Price(ExchangeMarket plugin) {
		this.plugin = plugin;
	}
	private String F(String string, Object... args) {
		return Localization.F(string, args);
	}

	private String L(String string) {
		return Localization.L(string);
	}
	/**/
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		if (!plugin.commands.hasCommandPermission(sender, "exchangemarket.price")) {
			return true;
		}
		if (args.length < 2 && player.getItemInHand().getTypeId() == 0) {
			
			plugin.sendMessage(sender, " �a/" + commandLabel + " price [itemName] [amount] [sale/buy] �7- " + L("cmdPriceDesc"));
			return true;
		}

		ItemStack item = null;
		int amount = 1;
		if (args.length > 1) {
			item = ItemDb.getItemStack(args[1]);
		} else {
			item = player.getItemInHand();
			amount = item.getAmount();
		}

		
		
		if (item == null) {
			plugin.sendMessage(sender, F("invalidItem", args[1]));
			return true;
		}

		String itemEnchants = MaterialUtil.Enchantment.encodeEnchantment(item);
		
		if (args.length > 2) {

			if (Utils.isInt(args[2])) {
				amount = Integer.parseInt(args[2]);
			} else {
				plugin.sendMessage(sender, F("invalidAmount", args[2]));

				return true;
			}
		}

		item.setAmount(amount);
		// plugin.sendMessage(sender, "amount: " + amount);

		int type = 0;
		if (args.length > 3) {
			if (args[3].equalsIgnoreCase("sell")) {
				type = 1;
			} else if (args[3].equalsIgnoreCase("buy")) {
				type = 2;

			} else {
				plugin.sendMessage(sender, F("invalidType", args[3]));
				return true;
			}
		}

		Database.itemStats stats = plugin.database.getItemStats(item.getTypeId(), item.getDurability(), itemEnchants, type);
		String itemName = plugin.itemdb.getItemName(item.getTypeId(), item.getDurability());

		plugin.sendMessage(sender, F("itemShort", itemName, amount));
		plugin.sendMessage(sender, plugin.commands.getItemStatsMsg(stats, amount));

		return true;
	}



}
