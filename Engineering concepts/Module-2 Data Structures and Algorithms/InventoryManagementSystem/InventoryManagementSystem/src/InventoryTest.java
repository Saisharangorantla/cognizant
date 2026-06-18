
public class InventoryTest {
    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        manager.addProduct(new Product(101, "Laptop", 10, 55000));
        manager.addProduct(new Product(102, "Mouse", 50, 500));
        manager.addProduct(new Product(103, "Keyboard", 25, 1200));

        System.out.println("Initial Inventory:");
        manager.displayProducts();

        manager.updateProduct(102, 60, 550);

        System.out.println("\nAfter Update:");
        manager.displayProducts();

        manager.deleteProduct(103);

        System.out.println("\nAfter Delete:");
        manager.displayProducts();

        System.out.println("\nSearch Product:");
        System.out.println(manager.searchProduct(101));
    }
}
