import { State, Action, StateContext } from '@ngxs/store';
import { Contact } from '../models/contact.model';

export class AddContact {
  static readonly type = '[Contact] Add';
  constructor(public payload: Contact) {}
}

export class UpdateContact {
  static readonly type = '[Contact] Update';
  constructor(public payload: Contact) {}
}

export class DeleteContact {
  static readonly type = '[Contact] Delete';
  constructor(public id: string) {}
}

export class LoadContacts {
  static readonly type = '[Contact] Load';
}

export interface ContactStateModel {
  contacts: Contact[];
  loading: boolean;
  error: string | null;
}

@State<ContactStateModel>({
  name: 'contacts',
  defaults: {
    contacts: [],
    loading: false,
    error: null
  }
})
export class ContactState {
  @Action(AddContact)
  addContact(ctx: StateContext<ContactStateModel>, action: AddContact) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      contacts: [...state.contacts, action.payload]
    });
  }

  @Action(UpdateContact)
  updateContact(ctx: StateContext<ContactStateModel>, action: UpdateContact) {
    const state = ctx.getState();
    const updatedContacts = state.contacts.map(contact => 
      contact.id === action.payload.id ? action.payload : contact
    );
    ctx.setState({
      ...state,
      contacts: updatedContacts
    });
  }

  @Action(DeleteContact)
  deleteContact(ctx: StateContext<ContactStateModel>, action: DeleteContact) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      contacts: state.contacts.filter(contact => contact.id !== action.id)
    });
  }
} 