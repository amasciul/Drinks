package fr.masciulli.drinks.view;

public interface ItemClickListener<T> {
    void onItemClick(int position, T item);
}
