package models.visitor;

public interface Visitable {
	void accept(Visitor visitor);
}
