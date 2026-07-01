	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (handIn != EnumHand.OFF_HAND) {
			BlockPos pos = playerIn.getPosition();
			BlockPos range1 = pos.add(-1, -1, -1);
			BlockPos range2 = pos.add(1, 3, 1);
			List<EntityArrow> arrows = worldIn.getEntitiesWithinAABB(EntityArrow.class, new AxisAlignedBB(range1, range2));
        
        if (!arrows.isEmpty()) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("Arrows", 0);
            ItemStack quiverWithArrowsStack = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
            
            for (EntityArrow a : arrows) {
                if (nbt.getInteger("Arrows") < ItemQuiverWithArrows.MAX_SIZE) {
                    worldIn.removeEntity(a);
                    nbt.setInteger("Arrows", nbt.getInteger("Arrows") + 1);
                    continue;
                }
                quiverWithArrowsStack.setTagCompound(nbt);
                return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
            }
            quiverWithArrowsStack.setTagCompound(nbt);
            return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
        }
        
        // Verificar si hay flechas en el inventario
        int arrowsSlot = playerIn.inventory.getSlotFor(new ItemStack(Items.ARROW));
        
        // Verificar que el slot sea válido
        if (arrowsSlot >= 0) {
            NBTTagCompound nbt = new NBTTagCompound();
            ItemStack arrowStack = playerIn.inventory.getStackInSlot(arrowsSlot);
            int arrowStackSize = arrowStack.getCount();
            nbt.setInteger("Arrows", arrowStackSize);
            
            ItemStack quiverWithArrowsStack = new ItemStack(ItemInit.QUIVER_WITH_ARROWS);
            quiverWithArrowsStack.setTagCompound(nbt);
            playerIn.inventory.removeStackFromSlot(arrowsSlot);
            return new ActionResult<>(EnumActionResult.SUCCESS, quiverWithArrowsStack);
        }
        
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
}
